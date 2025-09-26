# AI review plugin for Gerrit Code Review

You can directly use poc-ai-review.jar file by putting it in `/plugin/` of your gerrit site.

Please generate API-key from Google AI studio https://aistudio.google.com/apikey
and update `gerrit.config` with:

```
[plugin "poc-ai-review"]
	api-key = <API-KEY>
```
