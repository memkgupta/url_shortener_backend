import { Kafka } from "kafkajs"
import pubsub from "./pubsub.js"
const kafka = new Kafka({
  clientId: 'analytics_window_consumer',
  brokers: process.env.KAFKA_SERVER_URLS.split(",").map(url=>url.trim())
})


const consumer = kafka.consumer({ groupId: 'analytic_window_consumer_group' })

const runConsumer = async () => {


  // Consuming
  await consumer.connect()
  await consumer.subscribe({ topic: 'analytic_window', fromBeginning: false})

  await consumer.run({
    eachMessage: async ({ topic, partition, message }) => {

      pubsub.emit("window_in",JSON.parse(message.value.toString()))
    },
  })
}

export default runConsumer;