# OpenAPIPractice

## APIドキュメントの生成

このリポジトリの OpenAPI 仕様書（`openapi.yml`）から API 仕様書（HTML）を生成するには、Node.js が使える環境で次のコマンドを実行。

```
npx @redocly/cli build-docs openapi.yml --output user_api_docs.html
```

コマンドはカレントディレクトリの `openapi.yml` を読み込み、`user_api_docs.html` を出力。
出力ファイル名は必要に応じて変更。

## 📁 ディレクトリ構成

### ディレクトリ概要

| ディレクトリ       | 役割                                         |
| ------------------ | -------------------------------------------- |
| `lambda_function/` | APIリクエスト処理・レスポンス返却            |
| `services/`        | ビジネスロジック・データ処理・外部連携       |
| `repository/`      | データCRUD・Model連携                        |
| `models/`          | ORMマッピング・DB接続                        |
| `models/entity/`   | 型定義・データ構造標準化                     |
| `types/`           | API型定義・イベント・レスポンス型            |
| `common/`          | ユーティリティ・定数定義・エラーハンドリング |

<details>
<summary><strong>lambda_function/ - Interface Layer</strong></summary>

```
lambda_function/
├── createCustomer/             # 患者作成Lambda
│   ├── api/                    # APIリクエスト処理Lambda
│   │   └── lambda_function.ts
│   └── executer/               # ビジネスロジック実行Lambda
│       └── lambda_function.ts
└── updateCustomer/             # 患者更新Lambda
    ├── api/                    # APIリクエスト処理Lambda
    │   └── lambda_function.ts
    └── executer/               # ビジネスロジック実行Lambda
        └── lambda_function.ts
```

</details>

<details>
<summary><strong>services/ - Business Logic Layer</strong></summary>

```
services/
├── create-customer-service.ts          # 患者作成サービス
└── update-customer-service.ts          # 患者更新サービス
```

</details>

<details>
<summary><strong>repository/ - Data Access Layer</strong></summary>

```
repository/
├── customer-repository.ts              # 患者関連データアクセス
└── store-repository.ts                 # 店舗関連データアクセス
```

</details>

<details>
<summary><strong>models/ - Database Model Layer</strong></summary>

```
models/
├── customer.ts                         # 患者モデル
├── customer-attribute.ts               # 患者属性モデル
├── store.ts                            # 店舗モデル
└── entity/                             # エンティティレイヤー
    ├── customer-entity.ts              # 患者エンティティ
    ├── customer-attribute-entity.ts    # 患者属性エンティティ
    └── store-entity.ts                 # 店舗エンティティ
```

</details>

<details>
<summary><strong>types/ - API Type Definitions Layer</strong></summary>

```
types/
└── customer.ts                         # 患者API型定義
    ├── CreateCustomerEvent              # 患者作成イベント型
    ├── UpdateCustomerEvent              # 患者更新イベント型
    └── CustomerResult                   # 患者レスポンス結果型
```

</details>

<details>
<summary><strong>common/ - Utility Layer</strong></summary>

```
common/
├── api-exception-handler.ts            # APIレイヤーエラーハンドリング
├── api-utility.ts                      # APIレイヤーユーティリティ
├── base-exception-handler.ts           # ベースエラーハンドリング
├── exceptions.ts                       # カスタム例外定義
├── executer-exception-handler.ts       # Executerレイヤーエラーハンドリング
├── executer-utility.ts                 # Executerレイヤーユーティリティ
├── logger.ts                           # ログ処理
└── optional-param.ts                   # オプションパラメータ処理
```

</details>

## 📐 レイヤードアーキテクチャ図

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                       Lambda Function Layer (Interface)                     │
│                         AWS Lambda Entry Points                             │
│        API Request handling / Response return / Executer Lambda calls       │
└─────────────────────────────────────────────────────────────────────────────┘
                                      ▲ ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                         Service Layer (Business Logic)                      │
│                           Business Logic Implementation                     │
│   Customer validation / Data processing / Business rule enforcement         │
└─────────────────────────────────────────────────────────────────────────────┘
                                      ▲ ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                        Repository Layer (Data Access)                       │
│                           Data Access Abstraction                           │
│                     SQL execution / Data CRUD operations                    │
└─────────────────────────────────────────────────────────────────────────────┘
                                      ▲ ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                      Model Layer (Database Models)                          │
│                         Database Table Mapping                              │
│                      Database Access / ORM Operations                       │
└─────────────────────────────────────────────────────────────────────────────┘
                                      ▲ ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                              Database                                       │
│                         (RDS MySQL with Proxy)                              │
└─────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────┐
│                           Entity Layer (DTO)                                │
│                         Model Type Definitions                              │
│        (Accessible from Model / Repository / Service layers)                │
└─────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────┐
│                         Types Layer (API Types)                             │
│                    API Request/Response Type Definitions                    │
│       (Accessible from Lambda Function / Service layers)                    │
└─────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────┐
│                          Common Layer (Utilities)                           │
│                    Shared Functions / Error Handling                        │
│          Exception handlers / Logger / Validation utilities                 │
└─────────────────────────────────────────────────────────────────────────────┘

                           🌐 External Systems & Services
           ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ ┌─────────────┐
           │  Dental     │ │ API Gateway │ │ CloudWatch  │ │    WAF      │
           │  Systems    │ │             │ │   Logs      │ │ Protection  │
           └─────────────┘ └─────────────┘ └─────────────┘ └─────────────┘
```

**アクセス制約ルール:**

- 🔒 **Service Layer** : Lambda Function からのみアクセス可能
- 🔒 **Repository Layer** : Service からのみアクセス可能
- 🔒 **Model Layer** : Repository からのみアクセス可能
- 🔓 **Entity Layer** : Model / Repository / Service からのみ参照可能（DTOとして利用）
- 🔓 **Types Layer** : Lambda Function / Service からのみ参照可能（API型として利用）
- 🔓 **Common Layer** : 複数レイヤーから参照可能

## 📖 各レイヤーの詳細

### レイヤー概要表

| レイヤー          | 主な役割                 | アクセス可能なレイヤー             |
| ----------------- | ------------------------ | ---------------------------------- |
| **🔌 Interface**  | Lambdaエントリーポイント | Service, Types, Common             |
| **🧠 Service**    | ビジネスロジック実装     | Repository, Entity, Types, Common  |
| **🗳️ Repository** | データアクセス抽象化     | Model, Entity, Common              |
| **🗃️ Model**      | データベース表現         | Database, Entity, Common           |
| **📦 Entity**     | データ転送オブジェクト   | 制約なし（参照のみ）               |
| **🏷️ Types**      | API型定義                | 制約なし（参照のみ）               |
| **🛠️ Common**     | 共通機能提供             | 制約なし（全レイヤーから参照可能） |

<details>
<summary><strong>Interface Layer (Lambda Function) - 詳細</strong></summary>

**役割**

- AWS Lambdaのエントリーポイント（2段構成）

**主要責務**

- **API Lambda**: APIリクエストの受信・認証・バリデーション・Executer Lambda呼び出し
- **Executer Lambda**: ビジネスロジックの実行・レスポンス生成

**アクセス制約**

- ✅ Service Layer にアクセス可能
- ✅ Common Layer にアクセス可能
- ❌ 他のレイヤーへの直接アクセス禁止

**実装ファイル**

- `lambda_function/*/api/lambda_function.ts` (API Lambda)
- `lambda_function/*/executer/lambda_function.ts` (Executer Lambda)

**特徴**

- 2段構成によりAPI処理とビジネスロジックを分離
- IP制限・Basic認証をAPI Lambdaで実装
- ビジネスロジックはExecuter Lambdaで実行
</details>

<details>
<summary><strong>Service Layer (Business Logic) - 詳細</strong></summary>

**役割**

- ビジネスロジックの実装・制御

**主要責務**

- 患者データのバリデーション（Zod使用）
- 店舗認証・アクセス制御
- management_id重複チェック
- データ整合性・ビジネスルール適用

**アクセス制約**

- ✅ Repository Layer にアクセス可能
- ✅ Entity Layer にアクセス可能
- ✅ Common Layer にアクセス可能
- ❌ Model Layerへの直接アクセス禁止

**実装ファイル**

- `services/create-customer-service.ts`
- `services/update-customer-service.ts`

**特徴**

- Zodスキーマによる厳格なリクエストバリデーション
- 店舗別認証システムの実装
- エラーハンドリング・例外処理の統一
</details>

<details>
<summary><strong>Repository Layer (Data Access) - 詳細</strong></summary>

**役割**

- データアクセスの抽象化・統合

**主要責務**

- 患者・店舗データのCRUD操作
- Model Layerとの連携・SQL実行
- データアクセスパターンの統一化

**アクセス制約**

- ✅ Model Layer にアクセス可能
- ✅ Entity Layer にアクセス可能
- ✅ Common Layer にアクセス可能

**実装ファイル**

- `repository/customer-repository.ts`
- `repository/store-repository.ts`

**特徴**

- Modelクラスを経由したデータベースアクセス
- エラーハンドリング・例外処理の統一
- トランザクション管理
</details>

<details>
<summary><strong>Model Layer (Database) - 詳細</strong></summary>

**役割**

- データベーステーブルの表現・管理

**主要責務**

- ORMマッピング・テーブル定義
- データベースとの直接的なやり取り
- データ整合性・制約の管理

**アクセス制約**

- ✅ Database にアクセス可能
- ✅ Entity Layer にアクセス可能
- ✅ Common Layer にアクセス可能

**実装ファイル**

- `models/customer.ts`
- `models/customer-attribute.ts`
- `models/store.ts`

**特徴**

- MySQL2を使用したデータベースアクセス
- 静的メソッドによるCRUD操作提供
- フィールド名定義による型安全性確保
</details>

<details>
<summary><strong>Entity Layer (DTO) - 詳細</strong></summary>

**役割**

- データ転送オブジェクト（DTO）の定義

**主要責務**

- Model型定義・データクラス管理
- データ構造の標準化
- 型安全性の確保・インターフェース定義

**特徴**

- ビジネスロジックを持たない純粋なデータ構造
- TypeScriptインターフェースを使用した型定義
- 型チェックによるコンパイル時エラー検出

**アクセス制約**

- 🔓 制約なし（参照専用・他のレイヤーから参照される）

**実装ファイル**

- `models/entity/customer-entity.ts`
- `models/entity/customer-attribute-entity.ts`
- `models/entity/store-entity.ts`
</details>

<details>
<summary><strong>Types Layer (API Types) - 詳細</strong></summary>

**役割**

- API型定義・リクエスト/レスポンス型の定義

**主要責務**

- Lambda Function間の型安全な通信保証
- APIリクエスト・レスポンスの型定義
- データ変換・バリデーション用の型情報提供

**特徴**

- Lambda Function Layer専用の型定義
- TypeScriptインターフェースによる型チェック
- APIコントラクトの明確化・文書化

**型定義**

- **CreateCustomerEvent**: 患者作成APIのリクエスト型
- **UpdateCustomerEvent**: 患者更新APIのリクエスト型
- **CustomerResult**: 患者レスポンスの結果型

**アクセス制約**

- 🔓 制約なし（参照専用・Lambda Function / Service からアクセス）

**実装ファイル**

- `types/customer.ts`
</details>

<details>
<summary><strong>Common Layer (Utilities) - 詳細</strong></summary>

**役割**

- 共通機能・ユーティリティの提供

**主要責務**

- エラーハンドリング・例外処理
- ログ処理・デバッグ支援
- APIレスポンス生成・フォーマット
- ユーティリティ関数・ヘルパー関数

**特徴**

- 全レイヤーから参照可能
- 依存関係を持たない独立したユーティリティ
- レイヤー別エラーハンドリング

**アクセス制約**

- 🔓 制約なし（全レイヤーから参照可能）

**実装ファイル**

- `common/api-exception-handler.ts`
- `common/executer-exception-handler.ts`
- `common/logger.ts`
- `common/exceptions.ts`
</details>
