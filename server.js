const express = require("express");
const path = require("path");
const fs = require("fs");

const app = express();
app.use(express.json());

/*
  Generated videos ko is folder mein rakhein:
  /generated-videos/
*/

const VIDEO_DIR = path.join(__dirname, "generated-videos");
const THUMB_DIR = path.join(__dirname, "generated-thumbnails");

if (!fs.existsSync(VIDEO_DIR)) {
  fs.mkdirSync(VIDEO_DIR, { recursive: true });
}

if (!fs.existsSync(THUMB_DIR)) {
  fs.mkdirSync(THUMB_DIR, { recursive: true });
}

// Download final MP4
app.get("/api/download/video/:filename", (req, res) => {
  const filename = path.basename(req.params.filename);
  const filePath = path.join(VIDEO_DIR, filename);

  if (!fs.existsSync(filePath)) {
    return res.status(404).json({
      error: "Video not found"
    });
  }

  res.download(
    filePath,
    filename,
    {
      headers: {
        "Content-Type": "video/mp4"
      }
    },
    (err) => {
      if (err && !res.headersSent) {
        res.status(500).json({
          error: "Download failed"
        });
      }
    }
  );
});

// Download thumbnail
app.get("/api/download/thumbnail/:filename", (req, res) => {
  const filename = path.basename(req.params.filename);
  const filePath = path.join(THUMB_DIR, filename);

  if (!fs.existsSync(filePath)) {
    return res.status(404).json({
      error: "Thumbnail not found"
    });
  }

  res.download(filePath, filename);
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`AI YouTube Automation server running on port ${PORT}`);
});
