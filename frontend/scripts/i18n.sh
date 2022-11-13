#bin/bash
echo "Download i18n files from tolgee";

curl -O -J -L --request GET 'https://app.tolgee.io/v2/projects/export?languages=de-DE&zip=true&format=JSON' \
--header 'X-API-Key: '$TOLGEE_DOWNLOAD_API_KEY

echo "Unzip i18n files"
unzip -o test-result.zip -d i18n
rm test-result.zip

echo "i18n installed. Lets translate ...."