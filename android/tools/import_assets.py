#!/usr/bin/env python3
"""Regenerate the app's bundled media from the website's uploads/ folder.

The site serves full-size camera originals (up to ~6 MB each). The app ships resized,
EXIF-rotated copies instead, so the APK stays small and phones decode them quickly.

Run from the repository root after adding or replacing photos in uploads/:

    python3 android/tools/import_assets.py

Needs Pillow (pip install pillow) and ffmpeg (for the timelapse videos).
When you add a photo, add it to PHOTOS or MEMBERS below and reference the new
R.drawable name from SiteContent.kt.
"""
import os
import shutil
import subprocess
import sys

from PIL import Image, ImageOps

ROOT = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
UPLOADS = os.path.join(ROOT, "uploads")
RES = os.path.join(ROOT, "android", "app", "src", "main", "res")
CREAM = (248, 243, 241)
LOGO_NAVY = (16, 36, 46)

# drawable name -> file in uploads/ (the same files the website pages reference)
PHOTOS = {
    "photo_hero": "DSC_5283 copy.jpeg",
    "photo_workshop_table": "DSC_5431.jpeg",
    "photo_parent_laptop": "DSC_5557.jpeg",
    "photo_ewaste": "DSC_5545.jpeg",
    "photo_toolkit": "DSC_5280.jpeg",
    "photo_marking_screws": "DSC_5293 copy.jpeg",
    "photo_tweezers": "DSC_5305.jpeg",
    "photo_camera": "DSC_5423.jpeg",
    "photo_macbook": "DSC_5549.jpeg",
    "photo_senior_session": "IMG_1492.jpg",
    "photo_laptop_donation": "DSC_5527.jpeg",
}

MEMBERS = {
    "member_ronit": "IMG_3583.JPG",
    "member_tanay": "TanayMangal_shrewsbury.PNG",
    "member_nathan": "NathanAnwin_shrewsbury.jpeg",
    "member_ekansh": "EkanshJain_shrewsbury.jpeg",
    "member_suhrit": "SuhritGhosh_shrewsbury.jpeg",
    "member_howie": "HowieCao_westborough_chapterlead.jpeg",
    "member_atharv": "AtharvMishra_westborough_programslead.jpeg",
    "member_cullen": "CullenBautista_westborough_outreachlead.jpeg",
    "member_niti": "NitiTyagi_northborough_chapterlead.jpeg",
    "member_bhoomi": "BhoomiPrashanth_northbrough_programslead.jpeg",
    "member_dhriti": "DhritiKrishnaswamy_northborough_operations:outreachlead.jpeg",
    "member_ram": "RamKondapalli_grafton_chapterlead.jpg",
    "member_puneeth": "PuneethNunna_grafton_programslead.jpeg",
    "member_kevin": "KevinLee_grafton_operationslead.JPG",
    "member_param": "ParamTyagi_grafton.jpeg",
}

VIDEOS = {
    "timelapse_home": "img-1958_SLZ1QMcd.mp4",
    "timelapse_programs": "img-1863_xOX0vVKD.mp4",
}

LAUNCHER_DENSITIES = {"mdpi": 1, "hdpi": 1.5, "xhdpi": 2, "xxhdpi": 3, "xxxhdpi": 4}


def load(name):
    image = ImageOps.exif_transpose(Image.open(os.path.join(UPLOADS, name)))
    if image.mode in ("RGBA", "LA", "P"):
        image = image.convert("RGBA")
        flattened = Image.new("RGB", image.size, CREAM)
        flattened.paste(image, mask=image.split()[-1])
        image = flattened
    return image.convert("RGB")


def main():
    drawables = os.path.join(RES, "drawable-nodpi")
    os.makedirs(drawables, exist_ok=True)

    for name, source in PHOTOS.items():
        image = load(source)
        image.thumbnail((1400, 1400), Image.LANCZOS)
        image.save(os.path.join(drawables, f"{name}.jpg"), "JPEG", quality=80, optimize=True)

    for name, source in MEMBERS.items():
        image = load(source)
        side = min(image.size)
        left, top = (image.width - side) // 2, (image.height - side) // 2
        image = image.crop((left, top, left + side, top + side)).resize((480, 480), Image.LANCZOS)
        image.save(os.path.join(drawables, f"{name}.jpg"), "JPEG", quality=85, optimize=True)

    logo = Image.open(os.path.join(UPLOADS, "Tech_Awareness_MA_Logo_New.png")).convert("RGB")
    logo.resize((256, 256), Image.LANCZOS).save(os.path.join(drawables, "logo_taa.png"), optimize=True)
    Image.open(os.path.join(UPLOADS, "logos-1785202318150.jpeg")).convert("RGB").save(
        os.path.join(drawables, "logo_ifixit.jpg"), "JPEG", quality=90
    )

    # Adaptive icon foreground: the logo (with its own navy ground) inside the safe zone.
    for density, scale in LAUNCHER_DENSITIES.items():
        size = int(108 * scale)
        inner = int(size * 0.74)
        canvas = Image.new("RGB", (size, size), LOGO_NAVY)
        canvas.paste(logo.resize((inner, inner), Image.LANCZOS), ((size - inner) // 2, (size - inner) // 2))
        folder = os.path.join(RES, f"mipmap-{density}")
        os.makedirs(folder, exist_ok=True)
        canvas.save(os.path.join(folder, "ic_launcher_foreground.png"), optimize=True)
    store = os.path.join(ROOT, "android", "store")
    os.makedirs(store, exist_ok=True)
    logo.resize((512, 512), Image.LANCZOS).save(os.path.join(store, "ic_launcher-playstore.png"), optimize=True)

    # The site's clips are 1080p HEVC; H.264 plays on every Android device and is far smaller.
    if shutil.which("ffmpeg") is None:
        sys.exit("ffmpeg not found; photos were updated but videos were skipped.")
    raw = os.path.join(RES, "raw")
    os.makedirs(raw, exist_ok=True)
    for name, source in VIDEOS.items():
        subprocess.run(
            [
                "ffmpeg", "-v", "error", "-y", "-i", os.path.join(UPLOADS, source), "-an",
                "-vf", "scale=576:-2", "-c:v", "libx264", "-profile:v", "main", "-pix_fmt", "yuv420p",
                "-crf", "31", "-preset", "slow", "-movflags", "+faststart", os.path.join(raw, f"{name}.mp4"),
            ],
            check=True,
        )


if __name__ == "__main__":
    main()
