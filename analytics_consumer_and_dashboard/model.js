import mongoose from "mongoose";

const entrySchmea = new mongoose.Schema({
    id:{type:String},
    url: {
        type: String,
        required: false, // equivalent to NON_NULL (optional, but only stored if value is not null)
      },
      startTime: {
        type: Date,
      },
      endTime: {
        type: Date,
      },
      agentMap: {
        type: Map,
        of: Number, // or mongoose.Schema.Types.Long if you're using Longs
        default: {},
      },
      deviceMap: {
        type: Map,
        of: Number,
        default: {},
      },
      geoMap: {
        type: Map,
        of: Number,
        default: {},
      },
      clicks: {
        type: Number,
      },
      timestamp: {
        type: Date,
        default: Date.now,
      }
},
{timeseries:{
  timeField:'timestamp'
},collection:"entry"});

const Entry = mongoose.model("Entry",entrySchmea);
export default Entry;