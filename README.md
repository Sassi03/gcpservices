# gcpservices
Spring boot application to extract document using google document AI

Install Google Cloud SDK:<br>

Download and install the Google Cloud SDK for your operating system.<br>

Open a terminal or command prompt and run:<br>
gcloud init<br>
Follow the prompts to log in to your Google Cloud account, select your project, and configure the default settings.<br>

After initializing the SDK, you can authenticate with Application Default Credentials using:<br>
gcloud auth application-default login<br>

Set the default project ID for your application:<br>
gcloud config set project YOUR_PROJECT_ID<br>
Replace YOUR_PROJECT_ID with your actual Google Cloud project ID.<br>

Verify your current configuration settings:<br>
gcloud config list
