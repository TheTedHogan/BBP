(ns bbp.db
  (:require [schema.core :as s :include-macros true]))

;; schema of app-db
(def schema {:greeting s/Str
             :main-view s/Keyword})

;; initial state of app-db
(def app-db {:greeting "Hello Clojure in iOS and Android!"
             :main-view :page-1})
