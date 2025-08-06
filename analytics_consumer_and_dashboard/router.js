import { Router } from "express";
import Entry from "./model.js";

const router = Router();

router.get("/dashboard/:id",async(req,res,next)=>{
        const url_id = req.params.id;
console.log(url_id)
        try{
            const pipeline = [
               
                  {
                    $group: {
                      _id: null,
                      totalClicks: { $sum: "$clicks" },
                      allAgents: { $push: "$agentMap" }
                    }
                  },
                  {
                    $project: {
                      totalClicks: 1,
                      agentMap: {
                        $reduce: {
                          input: "$allAgents",
                          initialValue: {},
                          in: {
                            $let: {
                              vars: {
                                current: { $objectToArray: "$$this" },
                                previous: { $objectToArray: "$$value" }
                              },
                              in: {
                                $arrayToObject: {
                                  $map: {
                                    input: { $concatArrays: ["$$previous", "$$current"] },
                                    as: "item",
                                    in: {
                                      k: "$$item.k",
                                      v: {
                                        $add: [
                                          {
                                            $ifNull: [
                                              {
                                                $getField: {
                                                  field: "$$item.k",
                                                  input: "$$value"
                                                }
                                              },
                                              0
                                            ]
                                          },
                                          "$$item.v"
                                        ]
                                      }
                                    }
                                  }
                                }
                              }
                            }
                          }
                        }
                      }
                    }
                  }
            ]
            const debug = await Entry.find({ url: url_id.toString() });
            console.log("Matching Docs:", debug);
                const aggregation = await Entry.aggregate(pipeline);
                console.log(JSON.stringify(pipeline));
                res.status(200).json({
                    success:true,data:aggregation
                })
        }
        catch(error){
            console.log(error);
            res.status(500).json({
                success:false,
                message:error.message
            })
        }


})

export default router