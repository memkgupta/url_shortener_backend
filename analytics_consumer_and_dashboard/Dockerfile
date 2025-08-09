# 1️⃣ Base image
FROM node:18-alpine

# 2️⃣ Set working directory inside container
WORKDIR /app

# 3️⃣ Copy package.json and package-lock.json first (for caching dependencies)
COPY package*.json ./

# 4️⃣ Install dependencies (production only by default)
RUN npm install --production

# 5️⃣ Copy the rest of the project files
COPY . .

# 6️⃣ Expose the application port
EXPOSE 8000

# 7️⃣ Start the app
CMD ["npm", "start"]
