from __future__ import annotations

from fastapi import FastAPI

from . import api

app = FastAPI(title="VO UfaDevConf (Python)")
app.include_router(api.router)
