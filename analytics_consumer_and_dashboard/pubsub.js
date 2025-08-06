import {EventEmitter} from "events"

class PubSub extends EventEmitter {}

const pubsub = new PubSub();

export default pubsub