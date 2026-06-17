import asyncio
import uvicorn

if __name__ == "__main__":
    # On configure explicitement la boucle d'événements standard d'asyncio
    loop = asyncio.new_event_loop()
    asyncio.set_event_loop(loop)

    config = uvicorn.Config("app.main:app", host="0.0.0.0", port=8000, loop="asyncio")
    server = uvicorn.Server(config)

    # On force l'exécution globale sur cette boucle unique
    loop.run_until_complete(server.serve())
