# OpenAPIPractice

## APIドキュメントの生成

このリポジトリの OpenAPI 仕様書（`openapi.yml`）から API 仕様書（HTML）を生成するには、Node.js が使える環境で次のコマンドを実行。

```
npx @redocly/cli build-docs openapi.yml --output user_api_docs.html
```

コマンドはカレントディレクトリの `openapi.yml` を読み込み、`user_api_docs.html` を出力。
出力ファイル名は必要に応じて変更。
