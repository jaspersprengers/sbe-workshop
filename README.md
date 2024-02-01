# sbe-workshop
Workshop for Specification by Example, given at Rabobank 27-9-2023

## Your mission, should you choose to accept
“For each user that starts the journey, we want to know the corporate structure for a given kvk, i.e., all the constituent relations with their relation IDs. Authenticated users should be handled a little differently from anonymous users. If you're logged in, we should have your company details on record, so all the organisations and subsidiaries we get back from the Chamber of Commerce API should be present in CRM. If it's not, raise an error. There may be more companies listed in Siebel, and these need to be added to the legal structure that is returned. Company details we find in Siebel are leading (such as the exact name), but if the kvk portal returns related companies with different registration numbers, raise an error. If you're unauthenticated, you might be an existing customer who didn't log in. In that case (i.e., if we can find a record in Siebel for your kvk), raise an error. You should log in properly. Otherwise, create a new record in Siebel for the parent company and all its daughters and return those Siebel Ids in the corporate structure”

## Highlight all the technical terms

## make it consistent
Get rid of synonyms and choose the non-technical term where possible

## Make it more readable
* Condense verbose phrases.
* Remove imprecise or subjective phrases
* Use spacing to separate requirements

## What are the inputs and outputs?

## Feature decomposition
* Distinguish between features and scenarios.
  Features relate to a distinct piece of functionality while scenarios to different tweaks in the ingredients that yield the same successful result, or an unsuccessful one.
* Focus on business relevance, not the non-functional requirements
* Focus on the API. Don't peek inside the box.

