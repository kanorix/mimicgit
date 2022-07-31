# Git風バージョン管理コマンドを作ってみよう！

## 作るコマンド

### メインのコマンドとして

- `migit`

### サブコマンド

- `add`
- `commit`
- `init`
- `restore`
- `switch`

## 環境構築

### 必要な環境

- [Docker](https://www.docker.com/get-started/)
- [VScode](https://code.visualstudio.com/download)

### 手順

1. このリポジトリを `git clone` する
1. VScodeの拡張機能[Remote - Containers](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers)を入れる
1. VScodeでcloneしたフォルダを開く
1. VScode左下の緑(Open a remote window)をクリックして「Reopen in container」を選択

## ディレクトリ構造

以下の初期化コマンドを使うことで作業ディレクトリを管理できるようになります。

```bash
$ migit init
```

- working_dir
  - `mimicgit/`
    - `objects/`
    - `refs/heads/`
    - `HEAD`
    - `index`
