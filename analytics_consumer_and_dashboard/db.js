import mongoose from "mongoose";

const connect = async()=>{
    const connectionState = mongoose.connection.readyState;
    if(connectionState==1){
        console.log("Already connected");
        return;
    }
    if(connectionState==2){
        console.log("Connecting...")
        
        
    }
    try{
    
      await  mongoose.connect(process.env.DB_URI
           
        );
        console.log(mongoose.connection.readyState)
        console.log("DB connected")
    }
    catch(err){
        console.error(err);
        throw new Error(err)
    }
}
export default connect;