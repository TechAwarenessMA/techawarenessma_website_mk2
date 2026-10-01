(() => {
  // Public services for this small static site. Replace with hosted services as traffic grows.
  const geocoderUrl = 'https://photon.komoot.io/api/';
  const routerUrl = 'https://router.project-osrm.org/table/v1/driving/';
  const nearbyDriveSeconds = 20 * 60;
  const chapters = [
    { id: 'shrewsbury', venue: 'Shrewsbury Public Library', address: '609 Main Street, Shrewsbury, MA 01545', coordinates: [-71.7127268, 42.2970212], schedule: 'Tuesdays, 7:30–8:30 PM' },
    { id: 'grafton', venue: 'Grafton Public Library', address: '35 Grafton Common, Grafton, MA 01519', coordinates: [-71.6844194, 42.2059793], schedule: 'Saturdays, 12:00–1:00 PM' },
  ];
  const addressCache = new Map();
  // Design Canvas replaces its source elements on mount; share the initial location request.
  let initialLocation;
  const validCoordinates = value => Array.isArray(value) && value.length === 2 &&
    value.every(Number.isFinite) && Math.abs(value[0]) <= 180 && Math.abs(value[1]) <= 90;

  class ChapterFinder extends HTMLElement {
    connectedCallback() {
      if (this.shadowRoot) return;
      this.requestId = 0;
      this.attachShadow({ mode: 'open' }).innerHTML = `
        <link rel="stylesheet" href="vendor/leaflet/leaflet.css">
        <style>
          :host{display:block;font-family:'Poppins',sans-serif;color:#2C3E50}
          *{box-sizing:border-box} [hidden]{display:none!important}
          .panel{background:#E7E1DE;border-radius:24px;padding:36px}
          .kicker{color:#D22B42;font-size:.75rem;font-weight:700;letter-spacing:.14em;text-transform:uppercase}
          h2{color:#181818;font-size:2rem;font-weight:800;line-height:1.1;text-transform:uppercase;margin:12px 0}
          p{font-size:.938rem;line-height:1.65;margin:12px 0}
          form{margin-top:24px}label{display:block;font-size:.875rem;font-weight:600;margin-bottom:8px}
          .layout{display:grid;grid-template-columns:minmax(320px,.85fr) minmax(400px,1.15fr);gap:32px;align-items:start}
          .controls{display:flex;gap:10px;flex-wrap:wrap;align-items:center}
          input{flex:1;min-width:260px;border:2px solid #2C3E50;border-radius:999px;padding:14px 20px;font:inherit;background:#F8F3F1;color:#181818}
          button,.directions{font:inherit;font-size:.875rem;font-weight:700;cursor:pointer;border:2px solid #181818;border-radius:999px;padding:14px 24px;background:#181818;color:#F8F3F1;text-decoration:none;display:inline-block}
          button:hover,.directions:hover{background:#D22B42;border-color:#D22B42;color:#F8F3F1}
          .secondary{background:transparent;color:#181818}
          button:disabled{opacity:.55;cursor:wait}
          :focus-visible{outline:3px solid #0B7DA8;outline-offset:3px}
          .note{font-size:.75rem}a{color:#2C3E50;text-decoration:underline}a:hover{color:#D22B42}
          .status{min-height:1.6em;font-size:.813rem}
          .choices{display:flex;flex-direction:column;gap:0;background:#F8F3F1;border-radius:16px;border:1px solid #c5bcb7;overflow:hidden;margin-top:12px}
          .choices button{display:flex;gap:12px;width:100%;text-align:left;border-radius:0;border:0;border-bottom:1px solid #E7E1DE;background:#F8F3F1;color:#181818;padding:12px 16px;font-weight:500}
          .choices button:last-child{border-bottom:0}.choices button:hover,.choices button:focus-visible{background:#E7E1DE}
          .choice-title{display:block;font-weight:600}.choice-detail{display:block;font-size:.75rem;color:#52616d;margin-top:3px}
          .number{display:inline-flex;align-items:center;justify-content:center;flex-shrink:0;width:24px;height:24px;border-radius:50%;background:#0B7DA8;color:white;font-size:.75rem;font-weight:700}
          .result{margin-top:24px;background:#F8F3F1;border-radius:20px;padding:24px}
          .start-prompt{padding-bottom:24px;margin-bottom:24px;border-bottom:1px solid #E7E1DE}.start-prompt p{margin:0 0 16px;font-weight:600;color:#181818}
          h3{font-size:1.5rem;line-height:1.3;color:#181818;margin:12px 0}
          .time{font-size:2rem;font-weight:800;color:#181818;margin:0;line-height:1.2}
          .facts{display:grid;grid-template-columns:1fr 1fr;gap:20px;margin:20px 0}.facts dt{font-size:.688rem;text-transform:uppercase;letter-spacing:.1em;font-weight:700;margin-bottom:8px}.facts dd{margin:0;font-weight:600;color:#181818}
          .map{height:440px;border-radius:20px;background:#dcded6;isolation:isolate}.map-side{position:sticky;top:20px}.legend{display:flex;gap:20px;flex-wrap:wrap;font-size:.75rem;margin:12px 0 0}.legend span{display:flex;align-items:center;gap:7px}.dot{width:10px;height:10px;background:#D22B42;border-radius:50%}.dot.origin{background:#0B7DA8}
          .leaflet-container{font-family:'Poppins',sans-serif}.leaflet-tooltip{font-size:.75rem;font-weight:600}.leaflet-control-attribution{font-size:10px}.leaflet-control-attribution a{color:#2C3E50}.leaflet-bar a{color:#181818}.leaflet-container .leaflet-control-zoom a:hover{color:#181818}
          .pin{border-radius:50%;background:#D22B42;border:3px solid white;color:white;display:flex;align-items:center;justify-content:center;font-weight:700;box-shadow:0 2px 6px #0004;font-size:12px}.pin.origin{background:#0B7DA8}.pin.recommended{background:#181818}.leaflet-marker-icon:focus-visible{outline:3px solid #0B7DA8;outline-offset:3px}
          @media(max-width:900px){.layout{grid-template-columns:1fr}.map-side{position:static}.map{height:340px}}
        </style>
        <div class="panel">
          <div class="kicker">A workshop near you</div>
          <h2>Find your nearest chapter.</h2>
          <p>Enter your address or use your current location.</p>
          <div class="layout">
          <div>
          <form>
            <label for="address">Starting location</label>
            <div class="controls">
              <input id="address" name="address" type="search" autocomplete="off" placeholder="Enter an address, town, or ZIP" required maxlength="250" aria-describedby="location-hint" aria-controls="matches" aria-expanded="false">
            </div>
            <div class="controls" style="margin-top:12px">
              <button class="secondary" type="button" data-location>Use my location</button>
              <button type="submit">Search →</button>
            </div>
          </form>
          <p class="note" id="location-hint">Location access is optional. <a href="Privacy.dc.html">Privacy</a></p>
          <p class="status" role="status" aria-live="polite"></p>
          <div class="choices" id="matches" aria-label="Matching addresses" hidden></div>
          <div class="result" hidden></div>
          </div>
          <div class="map-side">
            <div class="map" role="region" aria-label="Map of starting locations and library chapters"></div>
            <div class="legend"><span><i class="dot origin"></i>Your starting location</span><span><i class="dot"></i>Library chapters</span></div>
            <p class="map-message note" hidden></p>
          </div>
          </div>
        </div>`;
      this.root = this.shadowRoot;
      this.root.querySelector('form').addEventListener('submit', event => {
        event.preventDefault();
        this.findAddress();
      });
      this.root.querySelector('[data-location]').addEventListener('click', () => this.findLocation());
      const input = this.root.querySelector('input');
      input.addEventListener('input', () => {
        clearTimeout(this.searchTimer);
        this.selectedOrigin = null;
        this.begin('');
        this.clearStartingMarkers();
        if (input.value.trim().length >= 3) this.searchTimer = setTimeout(() => this.findAddress(false), 450);
      });
      input.addEventListener('keydown', event => {
        if (event.key === 'ArrowDown') {
          const first = this.root.querySelector('.choices:not([hidden]) button');
          if (first) { event.preventDefault(); first.focus(); }
        }
        if (event.key === 'Escape') { this.root.querySelector('.choices').hidden = true; input.setAttribute('aria-expanded', 'false'); }
      });
      this.initMap();
      this.findLocation(true);
    }

    disconnectedCallback() {
      this.requestId++;
      this.controller?.abort();
      clearTimeout(this.searchTimer);
      this.resizeObserver?.disconnect();
      this.map?.remove();
    }

    begin(message) {
      this.controller?.abort();
      this.controller = new AbortController();
      this.root.querySelector('.choices').hidden = true;
      this.root.querySelector('input').setAttribute('aria-expanded', 'false');
      this.root.querySelector('.result').hidden = true;
      this.status(message);
      this.busy(false);
      return ++this.requestId;
    }

    status(message) { this.root.querySelector('.status').textContent = message; }
    busy(value) {
      this.root.querySelector('.panel').setAttribute('aria-busy', String(value));
    }
    current(id) { return id === this.requestId && this.isConnected; }

    initMap() {
      if (!window.L) { this.mapMessage('The map could not load. You can still search for a chapter.'); return; }
      this.map = L.map(this.root.querySelector('.map'), { scrollWheelZoom: false, zoomAnimation: false, fadeAnimation: false, markerZoomAnimation: false });
      const tiles = L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '© <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>',
      }).addTo(this.map);
      tiles.on('tileerror', () => this.mapMessage('Map images are unavailable right now. Address search still works.'));
      this.startingMarkers = L.layerGroup().addTo(this.map);
      this.chapterMarkers = chapters.map(chapter => {
        const marker = this.marker(chapter.coordinates, chapter.id === 'grafton' ? 'G' : 'S', '', chapter.venue, true).addTo(this.map);
        return marker;
      });
      this.map.fitBounds(chapters.map(chapter => this.latLng(chapter.coordinates)), { padding: [48, 48], maxZoom: 12, animate: false });
      this.resizeObserver = new ResizeObserver(() => this.map?.invalidateSize());
      this.resizeObserver.observe(this.root.querySelector('.map'));
    }

    mapMessage(message) {
      const note = this.root.querySelector('.map-message');
      note.textContent = message;
      note.hidden = false;
    }
    latLng(coordinates) { return [coordinates[1], coordinates[0]]; }
    marker(coordinates, label, className, description, permanent = false) {
      const marker = L.marker(this.latLng(coordinates), {
        icon: L.divIcon({ className: 'pin ' + className, html: label, iconSize: [30, 30], iconAnchor: [15, 15] }),
        title: description, alt: description,
      });
      const text = document.createElement('span');
      text.textContent = description;
      return marker.bindTooltip(text, { direction: 'top', offset: [0, -12], permanent });
    }
    clearStartingMarkers() {
      this.startingMarkers?.clearLayers();
      this.chapterMarkers?.forEach(marker => marker.getElement()?.classList.remove('recommended'));
    }
    showStartingPoint(origin, label) {
      this.clearStartingMarkers();
      if (!this.map) return;
      this.marker(origin, '●', 'origin', label).addTo(this.startingMarkers);
      this.map.fitBounds([origin, ...chapters.map(chapter => chapter.coordinates)].map(point => this.latLng(point)), { padding: [48, 48], maxZoom: 13, animate: false });
    }
    chooseAddress(feature, label) {
      clearTimeout(this.searchTimer);
      const origin = feature.geometry.coordinates;
      this.selectedOrigin = { origin, label };
      this.root.querySelector('input').value = label;
      const id = this.begin('Finding your nearest chapter…');
      this.showStartingPoint(origin, label);
      this.route(origin, label, id);
    }

    async fetchJson(url) {
      const controller = this.controller;
      const timer = setTimeout(() => controller.abort(), 15000);
      try {
        const response = await fetch(url, { signal: controller.signal, credentials: 'omit' });
        if (!response.ok) throw new Error('The map service is unavailable. Try again shortly, or browse the chapters below.');
        return await response.json();
      } finally { clearTimeout(timer); }
    }

    async findAddress(submitted = true) {
      clearTimeout(this.searchTimer);
      const query = this.root.querySelector('input').value.trim();
      if (submitted && this.selectedOrigin) {
        const { origin, label } = this.selectedOrigin;
        const id = this.begin('Finding your nearest chapter…');
        this.route(origin, label, id);
        return;
      }
      const id = this.begin('Looking up your address…');
      if (!query) { this.status('Enter an address, town, or ZIP code.'); return; }
      this.busy(true);
      try {
        const key = query.toLowerCase();
        let matches = addressCache.get(key);
        if (!matches) {
          const url = new URL(geocoderUrl);
          url.search = new URLSearchParams({ q: query, limit: '5', lang: 'en', lat: '42.25', lon: '-71.7' });
          const data = await this.fetchJson(url);
          matches = (data.features || []).filter(feature => validCoordinates(feature.geometry?.coordinates));
          if (matches.length) addressCache.set(key, matches);
        }
        if (!this.current(id)) return;
        if (!matches.length) throw new Error('No matching address found. Try adding your street, town, and state.');
        // Always confirm the match so an ambiguous address never silently picks the wrong town.
        this.status('Select your address below or on the map.');
        const choices = this.root.querySelector('.choices');
        choices.replaceChildren();
        this.clearStartingMarkers();
        matches.forEach((feature, index) => {
          const p = feature.properties || {};
          const street = [p.housenumber, p.street].filter(Boolean).join(' ');
          const title = street || p.name || p.city || p.town || p.village || 'Starting location';
          const detail = [...new Set([p.city || p.town || p.village, p.state, p.postcode, p.countrycode !== 'US' && p.country].filter(Boolean))].filter(item => item !== title).join(', ');
          const label = [title, detail].filter(Boolean).join(', ');
          const button = document.createElement('button');
          button.type = 'button';
          const number = document.createElement('span'); number.className = 'number'; number.textContent = index + 1;
          const text = document.createElement('span');
          const primary = document.createElement('span'); primary.className = 'choice-title'; primary.textContent = title;
          const secondary = document.createElement('span'); secondary.className = 'choice-detail'; secondary.textContent = detail;
          text.append(primary, secondary); button.append(number, text);
          button.addEventListener('click', () => this.chooseAddress(feature, label));
          button.addEventListener('keydown', event => {
            const buttons = [...choices.querySelectorAll('button')];
            if (event.key === 'ArrowDown' || event.key === 'ArrowUp') {
              event.preventDefault();
              buttons[(index + (event.key === 'ArrowDown' ? 1 : buttons.length - 1)) % buttons.length].focus();
            }
            if (event.key === 'Escape') {
              choices.hidden = true;
              this.root.querySelector('input').setAttribute('aria-expanded', 'false');
              this.root.querySelector('input').focus();
            }
          });
          if (this.map) {
            const marker = this.marker(feature.geometry.coordinates, String(index + 1), 'origin', label).addTo(this.startingMarkers);
            marker.on('click', () => this.chooseAddress(feature, label));
            button.addEventListener('mouseenter', () => marker.openTooltip());
            button.addEventListener('focus', () => marker.openTooltip());
            button.addEventListener('mouseleave', () => marker.closeTooltip());
            button.addEventListener('blur', () => marker.closeTooltip());
          }
          choices.append(button);
        });
        choices.hidden = false;
        this.root.querySelector('input').setAttribute('aria-expanded', 'true');
        if (this.map) this.map.fitBounds(matches.map(feature => this.latLng(feature.geometry.coordinates)), { padding: [48, 48], maxZoom: 14, animate: false });
        if (submitted) choices.querySelector('button').focus();
      } catch (error) { if (this.current(id)) this.fail(error); }
      finally { if (this.current(id)) this.busy(false); }
    }

    findLocation(automatic = false) {
      clearTimeout(this.searchTimer);
      this.selectedOrigin = null;
      const id = this.begin('Getting your location… You can also type an address.');
      if (!window.isSecureContext || !navigator.geolocation) {
        this.status('Location access is unavailable here. Enter an address above.');
        return;
      }
      const requestLocation = () => new Promise((resolve, reject) => {
        navigator.geolocation.getCurrentPosition(resolve, reject, { enableHighAccuracy: false, timeout: 12000, maximumAge: 60000 });
      });
      const location = automatic ? (initialLocation ||= requestLocation()) : requestLocation();
      location.then(position => {
        if (!this.current(id)) return;
        const origin = [position.coords.longitude, position.coords.latitude];
        const label = 'Current location';
        this.root.querySelector('input').value = label;
        this.selectedOrigin = { origin, label };
        this.showStartingPoint(origin, label);
        this.route(origin, label, id);
        // Fill a readable nearby address without delaying the chapter recommendation.
        const url = new URL('/reverse', geocoderUrl);
        url.search = new URLSearchParams({ lat: origin[1], lon: origin[0], lang: 'en' });
        fetch(url, { credentials: 'omit', signal: AbortSignal.timeout(8000) }).then(response => response.ok ? response.json() : null).then(data => {
          if (!this.current(id)) return;
          const p = data?.features?.[0]?.properties;
          if (!p) return;
          const address = [[p.housenumber, p.street].filter(Boolean).join(' '), p.city || p.town || p.village, p.state].filter(Boolean).join(', ');
          if (address) this.root.querySelector('input').value = 'Near ' + address;
        }).catch(() => {});
      }, error => {
        if (!this.current(id)) return;
        this.status(error.code === 1 ? 'Location access was declined. Enter an address above.' : 'Could not get your location. Enter an address, or try location again.');
      });
    }

    async route(origin, label, id) {
      this.busy(true);
      this.status('Comparing driving times…');
      try {
        if (!validCoordinates(origin)) throw new Error('Could not read this location. Try an address instead.');
        const coordinates = [origin, ...chapters.map(chapter => chapter.coordinates)].map(point => point.join(',')).join(';');
        const data = await this.fetchJson(routerUrl + coordinates + '?sources=0&destinations=1;2&annotations=duration&radiuses=1000;1000;1000');
        if (!this.current(id)) return;
        const durations = data.durations?.[0];
        if (data.code !== 'Ok' || !Array.isArray(durations) || durations.length !== 2 || !durations.every(time => Number.isFinite(time) && time >= 0)) {
          throw new Error('Could not compare driving routes to both libraries. Try another starting address, or browse the chapters below.');
        }
        // Compare raw seconds, before rounding the displayed minutes. Exactly 60 seconds is a tie.
        const tied = Math.abs(durations[0] - durations[1]) <= 60;
        const winner = tied || durations[1] < durations[0] ? 1 : 0;
        this.showResult(origin, label, durations, winner, tied);
        this.status('');
      } catch (error) { if (this.current(id)) this.fail(error); }
      finally { if (this.current(id)) this.busy(false); }
    }

    fail(error) {
      this.status(error.name === 'AbortError' ? 'The map service took too long. Try again, or browse the chapters below.' : error.message || 'Could not load driving times. Please try again.');
    }

    showResult(origin, label, durations, winner, tied) {
      const chapter = chapters[winner];
      const result = this.root.querySelector('.result');
      result.replaceChildren();
      const add = (tag, text, className) => {
        const element = document.createElement(tag);
        element.textContent = text;
        if (className) element.className = className;
        result.append(element);
        return element;
      };
      const formatTime = seconds => seconds < 60 ? '<1 min' : Math.round(seconds / 60) + ' min';
      // Use the fastest raw travel time, even when Grafton wins the one-minute tie-break.
      const noNearbyChapter = Math.min(...durations) > nearbyDriveSeconds;
      if (noNearbyChapter) {
        const prompt = add('div', '', 'start-prompt');
        const message = document.createElement('p');
        message.textContent = 'No chapter within a 20-minute drive. Bring one to your town.';
        const start = document.createElement('a');
        start.className = 'directions';
        start.textContent = 'Start a chapter →';
        start.href = 'StartChapter.dc.html';
        prompt.append(message, start);
      }
      add('div', noNearbyChapter ? 'Closest existing chapter' : 'Your nearest chapter', 'kicker');
      add('h3', chapter.venue);
      add('p', chapter.address);
      const facts = document.createElement('dl'); facts.className = 'facts';
      const drive = document.createElement('div');
      const driveTitle = document.createElement('dt'); driveTitle.textContent = 'Estimated drive';
      const time = document.createElement('dd'); time.className = 'time'; time.textContent = formatTime(durations[winner]);
      drive.append(driveTitle, time);
      const workshop = document.createElement('div');
      const workshopTitle = document.createElement('dt'); workshopTitle.textContent = 'Workshop';
      const schedule = document.createElement('dd'); schedule.textContent = chapter.schedule;
      workshop.append(workshopTitle, schedule); facts.append(drive, workshop); result.append(facts);
      const directions = add('a', 'Directions ↗', 'directions');
      const url = new URL('https://www.google.com/maps/dir/');
      url.search = new URLSearchParams({ api: '1', origin: origin[1] + ',' + origin[0], destination: chapter.address, travelmode: 'driving' });
      directions.href = url.href;
      directions.target = '_blank';
      directions.rel = 'noopener noreferrer';
      add('p', 'Drive time excludes traffic.', 'note');
      result.hidden = false;
      this.chapterMarkers?.[winner]?.getElement()?.classList.add('recommended');
    }
  }
  if (!customElements.get('chapter-finder')) customElements.define('chapter-finder', ChapterFinder);
})();
