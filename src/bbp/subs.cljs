(ns bbp.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub]]))

(register-sub
  :get-greeting
  (fn [db _]
    (reaction
      (get @db :greeting))))

(register-sub
 :main-view
 (fn [db [sid formulas]]
   (reaction
    (get @db :main-view))))
