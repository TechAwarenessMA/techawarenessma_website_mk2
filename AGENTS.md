# Repository Guidelines

## Project Structure & Module Organization

This repository is a static Tech Awareness Association website built with Design Canvas-style HTML components. Root-level `*.dc.html` files are individual pages or reusable components: `index.html` is the home page, while `SiteNav.dc.html` and `SiteFooter.dc.html` are imported site-wide. Keep page-specific behavior close to its page; use `support.js` only for the generated Design Canvas runtime and do not edit it manually. `image-slot.js` provides the image-slot custom element. Store site media in `uploads/` and preserve descriptive filenames.

`android/` is the native Android app (Kotlin + Jetpack Compose) with the same content and features as the site; see `android/README.md`. Its copy is ported from these pages into `android/app/src/main/java/.../content/SiteContent.kt` and the certification files, so a content change on the site should be mirrored there. Its media is generated from `uploads/` with `python3 android/tools/import_assets.py`. `.vercelignore` keeps `android/` out of the Vercel deploy.

`_ds/organic-*/` is a vendored design-system reference. Treat its bundle, manifest, and stylesheet as generated/reference material unless a task explicitly targets that system. `handbook-text.txt` is source content for the chapter-certification material.

## Development & Validation

There is no `package.json`, build command, or automated test suite in this repository. Preview pages through the Design Canvas environment or serve the repository from a local static web server, for example:

```sh
python3 -m http.server 8000
```

Then inspect `http://localhost:8000/index.html` and every changed `*.dc.html` route. Check browser console errors, links and imports, interactive controls, keyboard focus, and the desktop layout. Pages currently use a fixed `min-width: 1100px`; do not claim mobile support without deliberately redesigning and testing it.

## Coding Style & Naming Conventions

Match the existing HTML-first style: two-space indentation inside markup, inline styles for page-local presentation, and camelCase for JavaScript variables and component data. Name reusable components in PascalCase (`SiteNav.dc.html`) and pages in clear PascalCase words (`Chapter Certification.dc.html`). Keep internal links relative and exact, e.g. `href="Programs.dc.html"`; URL-encode spaces in asset URLs. Preserve semantic HTML, meaningful `alt` text, `aria-*` attributes, and visible `:focus-visible` states.

## Testing Guidelines

Make manual verification proportional to the change. For shared navigation or footer changes, open every page that imports the component. For new media, confirm the path works with spaces and punctuation encoded. For scripts, exercise both normal interaction and no-JavaScript-safe content where applicable.

## Commit & Pull Request Guidelines

Recent history uses short, imperative summaries such as `Add real logo/photos...` and `Redesign`. Use a concise present-tense subject under 72 characters; avoid vague messages. Pull requests should state the affected pages, summarize visible changes, link related issues when available, and include screenshots for layout or visual updates. Call out changed assets, external links, or content that needs stakeholder review.
