FROM python:3.9-slim

# Set the working dir
WORKDIR /app

# Copy the current dir into the container
COPY requirements.txt ./

# Install packages
RUN pip install --no-cache-dir -r requirements.txt

COPY . .

# Run app.py when the container launches
CMD ["python", "app.py"]