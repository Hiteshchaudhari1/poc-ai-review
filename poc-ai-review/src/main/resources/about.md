# AI review plugin for Gerrit Code Review

## Introduction
poc-AI-Review plugin exposes an API which would help code reviewers to get the
insights about the code review of the current change. AI can suggest the
potential code changes to maintain a clean coding and design flaws if any.

For AI models, we are using poc's own AI platform
[poc AI Launchpad](https://pocit-core-playground-vole.ai-launchpad.prod.eu-central-1.aws.apps.ml.hana.ondemand.com/aic/index.html#/generativeaihub?workspace=poc-genai-xl&resourceGroup=default&/g/promptchat?modelId=gpt-4.1).
poc AI Launchpad is an integration which provides various AI models that a user can select
to get AI answers on the current change. For AI prompt, we will be reusing the existing feature of `Create AI Prompt`.

**Supported AI models**: `gemini-2.5-pro`.

Supported AI models and plugin_names are exposed in `getServerInfo` API.

---

### Sample AI review API

**Request:**

```bash 
curl --request POST \
  --url http://localhost:8083/a/changes/1 \
  --header 'authorization: Basic <Auth_token>' \
  --header 'charset: UTF-8' \
  --header 'content-type: application/json' \
  --data '{
  "model": "gemini-2.5-pro",
  "prompt": "You are a highly experienced code reviewer specializing in Git patches. Your task is to analyze the provided Git patch (`patch`) and provide comprehensive feedback...",
  "plugin_name": "poc-ai-review"
}'
```
**Response:**
```
)]}'
"This is a review of the provided Git patch.\n\n### Summary\n\nThe patch appears to be incomplete or incorrect. While the commit message states its purpose is to \"Add message for the \u0027Help Me Review\u0027 AI feature\", the actual change only introduces a new, empty file named `text.txt`. This discrepancy is a critical issue that needs to be addressed.\n\n### Critical Issues\n\n*   **Contradiction Between Commit Message and Content**\n    The primary issue is that the patch does not implement the change described in the commit message. It adds an empty file, not a message or any related functionality. This suggests the commit may have been made in error, perhaps by staging the wrong files or forgetting to stage the intended changes.\n\n    **Recommendation:** Please amend this commit to include the actual message or feature implementation. If this patch was created by mistake, it should be abandoned.\n\n### Minor Issues \u0026 Nits\n\n*   **Generic Filename**\n    The filename `text.txt` is highly generic and does not provide any context about its purpose. If this file is intended to be part of the feature, it should have a more descriptive name.\n\n    **Recommendation:** Choose a filename that reflects the file\u0027s content and purpose (e.g., `ai_review_feature_notes.md`, `help_message.txt`) and place it in an appropriate directory within the project structure.\n\n*   **Future Commit Date**\n    The commit date is set in the future (`Wed, 30 Jul 2025`). This is unusual and likely caused by an incorrect system clock on the author\u0027s machine. While not a functional bug, it can cause confusion when tracking project history.\n\n    **Recommendation:** Please check your system\u0027s date and time configuration to ensure future commits have an accurate timestamp.\n\n### Conclusion\n\nThis patch cannot be merged in its current state. The fundamental mismatch between the stated intent and the actual code changes must be resolved. Please revise the patch to include the intended feature."
```

## Configuration

The configuration of the plugin can be maintained in `gerrit.config` & `secure.config` file.

Example:
```
[plugin "poc-ai-review"]
	authUrl = <URL>
```
### poc AI Launchpad configuration properties:

_Prerequisite: AI model deployment exists in poc AI Launchpad._

Following properties are required to connect to poc AI Launchpad AI model's deployment:

`plugin.@PLUGIN@.gemini25ProUrl`: URL to the Gemini deployment model on poc AI Launchpad

`plugin.@PLUGIN@.authUrl`: URL to the authorization server to generate Oauth 2.0 bearer token received from poc AI Launchpad

`plugin.@PLUGIN@.clientId`: ClientId required to create Oauth2.0 Bearer token. 

`plugin.@PLUGIN@.clientSecret`: ClientSecret required to create Oauth2.0 Bearer token.
