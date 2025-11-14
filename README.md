# TODO API - Kotlin Spring Boot Application

Kotlin + Spring Boot + Gradle + Doma + PostgreSQL + Testcontainersで構築したTODOアプリケーションのREST APIです。

## 技術スタック

- **言語**: Kotlin 1.9.21
- **フレームワーク**: Spring Boot 3.2.0
- **ビルドツール**: Gradle 8.5
- **データベース**: PostgreSQL 15
- **ORM**: Doma 2.57.0
- **テスト**: JUnit 5, Testcontainers
- **コンテナ**: Docker, Docker Compose
- **インフラ**: AWS (EC2, VPC), Terraform

## プロジェクト構成

```
.
├── src/
│   ├── main/
│   │   ├── kotlin/com/example/todoapi/
│   │   │   ├── TodoApiApplication.kt                     # メインクラス
│   │   │   ├── application/service/                      # サービス層
│   │   │   │   └── TodoService.kt
│   │   │   ├── domain/                                   # ドメイン層
│   │   │   │   ├── model/
│   │   │   │   │   ├── Todo.kt
│   │   │   │   │   └── TodoId.kt
│   │   │   │   └── repository/
│   │   │   │       └── TodoRepository.kt
│   │   │   ├── infrastructure/                           # インフラ層
│   │   │   │   ├── doma/
│   │   │   │   │   ├── DomaConfig.kt
│   │   │   │   │   ├── dao/TodoDao.kt
│   │   │   │   │   └── entity/TodoEntity.kt
│   │   │   │   └── repository/
│   │   │   │       └── TodoRepositoryImpl.kt
│   │   │   └── presentation/                             # プレゼンテーション層
│   │   │       ├── controller/TodoController.kt
│   │   │       ├── dto/TodoDto.kt
│   │   │       └── exception/GlobalExceptionHandler.kt
│   │   └── resources/
│   │       ├── application.yml                           # アプリケーション設定
│   │       ├── application-docker.yml                    # Docker環境用設定
│   │       └── META-INF/com/example/todoapi/             # Doma SQLファイル
│   └── test/                                             # テストコード
├── sql/init/                                             # データベース初期化スクリプト
├── terraform/                                            # Terraformファイル
├── docker-compose.yml                                    # Docker Compose設定
├── Dockerfile                                            # アプリケーションDockerfile
└── build.gradle.kts                                      # Gradleビルド設定
```

## 環境構築

### ローカル開発環境

#### 必要なツール

- Docker & Docker Compose
- Java 17 (開発時のみ)
- Gradle 8.5 (開発時のみ、Gradle Wrapperを使用する場合は不要)

#### 起動手順

1. リポジトリのクローン

```bash
git clone <repository-url>
cd kotlin-practice
```

2. Docker Composeで起動

```bash
docker-compose up -d
```

3. アプリケーションの起動確認

```bash
curl http://localhost:8080/api/todos
```

4. 停止

```bash
docker-compose down
```

### AWS EC2環境のセットアップ

#### 前提条件

- AWS CLIの設定
- Terraformのインストール (v1.0以上)
- EC2キーペアの作成

#### デプロイ手順

1. Terraform変数ファイルの作成

```bash
cd terraform
cp terraform.tfvars.example terraform.tfvars
```

2. `terraform.tfvars`を編集

```hcl
aws_region       = "ap-northeast-1"
project_name     = "kotlin-todo-api"
environment      = "dev"
allowed_ssh_cidr = "YOUR_IP_ADDRESS/32"  # 自分のIPアドレスに変更
ec2_key_name     = "your-key-name"        # 作成したEC2キーペア名に変更
```

3. Terraformの実行

```bash
# 初期化
terraform init

# プランの確認
terraform plan

# リソースの作成
terraform apply
```

4. 出力されたEC2のパブリックIPアドレスを確認

```bash
terraform output ec2_public_ip
```

5. EC2インスタンスにSSH接続

```bash
ssh -i ~/.ssh/your-key-name.pem ubuntu@<EC2_PUBLIC_IP>
```

6. EC2上でアプリケーションをデプロイ

```bash
# リポジトリのクローン
git clone <repository-url>
cd kotlin-practice

# Docker Composeで起動
docker-compose up -d

# ログの確認
docker-compose logs -f app
```

7. APIの動作確認

```bash
curl http://<EC2_PUBLIC_IP>:8080/api/todos
```

## API エンドポイント

### TODO一覧取得

```bash
GET /api/todos
```

**レスポンス例:**
```json
[
  {
    "id": 1,
    "title": "Learn Kotlin",
    "description": "Study Kotlin programming language basics",
    "completed": false,
    "created_at": "2024-01-01T10:00:00",
    "updated_at": "2024-01-01T10:00:00"
  }
]
```

### TODO取得

```bash
GET /api/todos/{id}
```

### TODO作成

```bash
POST /api/todos
Content-Type: application/json

{
  "title": "New Task",
  "description": "Task description"
}
```

### TODO更新

```bash
PUT /api/todos/{id}
Content-Type: application/json

{
  "title": "Updated Task",
  "description": "Updated description",
  "completed": true
}
```

### TODO完了

```bash
PATCH /api/todos/{id}/complete
```

### TODO未完了

```bash
PATCH /api/todos/{id}/incomplete
```

### TODO削除

```bash
DELETE /api/todos/{id}
```

## テストの実行

### 全テストの実行

```bash
./gradlew test
```

### 特定のテストクラスの実行

```bash
./gradlew test --tests TodoServiceTest
```

### テストレポートの確認

```bash
open build/reports/tests/test/index.html
```

## 開発

### ローカルで実行（Dockerを使わない場合）

1. PostgreSQLを起動

```bash
docker-compose up -d postgres
```

2. アプリケーションを起動

```bash
./gradlew bootRun
```

### ビルド

```bash
./gradlew build
```

### Jarファイルの作成

```bash
./gradlew bootJar
```

生成されたJarファイル: `build/libs/todo-api-0.0.1-SNAPSHOT.jar`

## データベース

### スキーマ

```sql
CREATE TABLE todos (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

### 初期データ

`sql/init/02_insert_sample_data.sql`にサンプルデータが含まれています。

## トラブルシューティング

### ポートが既に使用されている

```bash
# 使用中のポートを確認
lsof -i :8080
lsof -i :5432

# プロセスを終了
kill -9 <PID>
```

### Dockerコンテナが起動しない

```bash
# ログを確認
docker-compose logs

# コンテナを再起動
docker-compose restart

# 完全にクリーンアップして再起動
docker-compose down -v
docker-compose up -d
```

### Gradleビルドエラー

```bash
# Gradleキャッシュをクリア
./gradlew clean

# 依存関係を再ダウンロード
./gradlew build --refresh-dependencies
```

## ライセンス

MIT License

## 作者

Your Name
