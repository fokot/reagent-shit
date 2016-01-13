(ns reagent-shit.core
  (:require [reagent.core :as r]))

(enable-console-print!)

(println "Edits to this text should show up in your developer console.")


;; define your app data so that it doesn't get over-written on reload

(defonce app-state (r/atom
  {:colors (range 0 255 10)}))

(defn rotate-list [list] 
  (concat (rest list) [(first list)]))

(defn animate [] 
  (swap! app-state update-in [:colors] rotate-list))

(defn toggle-amination [] 
  (if (:interval-id @app-state) 
    (do (. js/window (clearInterval (:interval-id @app-state)))
        (swap! app-state dissoc :interval-id))
    (swap! app-state assoc :interval-id (. js/window (setInterval animate 200)))
    ))

(defn button []
  [:button.button 
    {:type "button" 
     :on-click toggle-amination
     ;; #(swap! app-state update-in [:colors] rotate-list)
    }
    (if (:interval-id @app-state) "Stop animation" "Start amination") ])

(defn color-square [color] 
    [:div {:style { :background-color color :width "20px" :height "20px" }}])

(def colors-component 
  (fn [] [:div.colors-component
         (for [i (:colors @app-state)]
          ;[color-square [(str "rgb(250,235," i ")")]])]
          ^{:key i}[color-square [ (str "rgb(100," i "," i ")")]])]))

(defn start []
  (r/render-component 
   [:div
       [:h1 "This shit is awesome!"]
       [button]
       [colors-component]
   ]
   (.getElementById js/document "app")))

(start)


(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
