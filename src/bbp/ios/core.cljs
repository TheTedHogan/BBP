(ns bbp.ios.core
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [re-frame.core :refer [register-sub]]
            [bbp.handlers]
            [bbp.subs]))


(set! js/React (js/require "react-native"))

(def app-registry (.-AppRegistry js/React))
(def text (r/adapt-react-class (.-Text js/React)))
(def view (r/adapt-react-class (.-View js/React)))
(def image (r/adapt-react-class (.-Image js/React)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight js/React)))
(def text-box (r/adapt-react-class (.-TextInput js/React)))

(def logo-img (js/require "./images/cljs.png"))

(defn alert [title]
      (.alert (.-Alert js/React) title))

(defn page-1 []
  (let [greeting (subscribe [:get-greeting])
        v (subscribe [:main-view])]
    #_(.log js/console (name @view))

    (fn []
      [view {:style {:flex-direction "column" :margin 40 :align-items "center"}}
       [text {:style {:font-size 30 :font-weight "100" :margin-bottom 20 :text-align "center"}} @greeting]
       [text {:style {:font-size 30 :font-weight "100" :margin-bottom 20 :text-align "center"}} (name @v)]
       [image {:source logo-img
               :style  {:width 80 :height 80 :margin-bottom 30}}]
       [touchable-highlight {:style {:background-color "#999" :padding 10
                                     :border-radius 5 :margin-bottom 20}
                             :on-press #(alert "HELLO!")}
        [text {:style {:color "white" :text-align "center" :font-weight "bold"}} "press me"]]
       [text-box {:style {:height       40
                          :border-color "#999"
                          :border-width 1}
                  :on-change-text #(if (< 10 (count %))
                                     (do
                                       (dispatch [:set-greeting ""])
                                       (alert "Too Many Letters"))
                                     (dispatch [:set-greeting %]))}]
       [touchable-highlight {:style {:background-color "#999" :padding 10
                                     :border-radius 5 :margin-bottom 20}
                             :on-press #(dispatch [:page-change :page-2])}
        [text {:style {:color "white" :text-align "center" :font-weight "bold"}} "Next Page"]]])))

(defn page-2 []
  (let [v (subscribe [:main-view])]
    [view
     [text {:style {:font-size 30 :text-align "center"}} "Test" ]
     [touchable-highlight {:style {:background-color "#999" :padding 10
                                   :border-radius 5 :margin-bottom 20}
                           :on-press #(dispatch [:page-change :page-1])} [text "Go Back"]]]))

(defn get-view [v]
  (condp = v
    :page-1 page-1
    :page-2 page-2))

(defn app-root []
  (let [main-view (subscribe [:main-view])]
    (fn []
      [view {:style {:flex-direction "column" :margin 40 :align-items "center"}} [(get-view @main-view)]])))

(defn init []
      (dispatch-sync [:initialize-db])
      (.registerComponent app-registry "bbp" #(r/reactify-component app-root)))

