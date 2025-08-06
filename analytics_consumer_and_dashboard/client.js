import WebSocket from 'ws';

const ws = new WebSocket('ws://localhost:8000/ws');

ws.on('open', () => {
  console.log('connected');
  ws.send('Hello server!');
});

ws.on('update', (data) => {
  console.log(`Received: ${data}`);
});