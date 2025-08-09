import "dotenv/config"
import express from 'express'; 

import runConsumer from "./consumer.js";

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
 
    const client = clients.get(_event.shortUrl);
    if(!client)
    {
      
        return;
    }
    else{
      
        client.write(`data: ${JSON.stringify(_event)}\n\n`)
    }
})
app.listen(8000,()=>{
    console.log(
        "Server running great"
    )
})