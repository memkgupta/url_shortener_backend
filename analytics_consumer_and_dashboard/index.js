import "dotenv/config"
import express from 'express'; 
import connect from "./db.js";
import router from "./router.js";
import runConsumer from "./consumer.js";
import { Server } from "socket.io";
import {createServer} from "http"
import pubsub from "./pubsub.js";
import cors from "cors"
const app = express();
let clients = new Map();
runConsumer().catch((e)=>{
    console.log(e);
})
app.use(cors());


app.get("/click-events",(req,res)=>{
    res.setHeader("Content-Type","text/event-stream");
    res.setHeader("Cache-Control","no-cache");
    res.setHeader("Connection","keep-alive");
    res.flushHeaders();
    res.write(`data: ${JSON.stringify({ message: "SSE connected" })}\n\n`);
    clients.set(req.query.id,res);
     req.on("close", () => {
        clients.delete(req.query.id)
    });
})
pubsub.on("window_in",(_event)=>{
    console.log(_event)
    const client = clients.get(_event.shortUrl);
    if(!client)
    {
        console.log("Client not found",_event.shortUrl)
        return;
    }
    else{
        console.log("Mil gaya ",client);
        client.write(`data: ${JSON.stringify(_event)}\n\n`)
    }
})
app.listen(8000,()=>{
    console.log(
        "Server running fucking great"
    )
})