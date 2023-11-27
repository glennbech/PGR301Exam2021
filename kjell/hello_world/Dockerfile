FROM python:3.9-slim

# Set the working dir
WORKDIR /app

# Copy the req.txt
COPY requirements.txt ./

# Install packages
RUN pip install --no-cache-dir -r requirements.txt

# Copy the current dir into the container
COPY . .

# Run app.py when the container launches
CMD ["python", "app.py"]
