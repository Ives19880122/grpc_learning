# grpc-class

- [參考github](https://github.com/vinsguru/grpc-java-course/tree/master)

## Sec01

- 建立基本的proto

## Sec02

- 可設定multiFile 讓類別產生與Sec01差異
- proto特性 equals能比較, ==會失敗, 物件不可變, 不可為null

### 型別

- int 對應 int32 / sint32(負值)
- long 對應 int64 / sint64(負值)
- float 對應 float
- double 對應 double
- boolean 對應 bool
- String 對應 string
- byte[] 對應 bytes

### 組成

- 同時在一個proto宣告不只一組message

### 集合

- List 用 repeated
- Map 用 map

### enum

- 序號從0開始宣告

### defaultValues

- 可以透過has檢查特定欄位
- 預設值寫在proto內
- enum的預設值是0

### 抽象 OneOf

- 泛型有多個子類型
- 會自動創出enum

### optional

- message內屬性的修飾字,表示不一定需要

### 比較版本差異

- proto欄位可以隨意改名,但不要改變型別
- 不同版本的解析可以依據相同位置相同型別來解析
- 不存在的型別被當成預設

### meta data

- Headers
- content-type: application/grpc

