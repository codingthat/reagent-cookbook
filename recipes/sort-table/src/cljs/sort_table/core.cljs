(ns sort-table.core
    (:require [reagent.core :as reagent :refer [atom]]))

(def state (atom {:sort-val "first-name" :ascending true}))

(def table-contents
  [{:id 1 :first-name "Bram"    :last-name "Moolenaar"  :known-for "Vim"}
   {:id 2 :first-name "Richard" :last-name "Stallman"   :known-for "GNU"}
   {:id 3 :first-name "Dennis"  :last-name "Ritchie"    :known-for "C"}
   {:id 4 :first-name "Rich"    :last-name "Hickey"     :known-for "Clojure"}
   {:id 5 :first-name "Guido"   :last-name "Van Rossum" :known-for "Python"}
   {:id 6 :first-name "Linus"   :last-name "Torvalds"   :known-for "Linux"}
   {:id 7 :first-name "Yehuda"  :last-name "Katz"       :known-for "Ember"}])

(defn update-sort-value [new-val]
  (if (= new-val (:sort-val @state))
    (swap! state assoc :ascending (not (:ascending @state)))
    (swap! state assoc :ascending true))
  (swap! state assoc :sort-val new-val))

(defn sorted-contents []
  (let [sorted-contents (sort-by (keyword (:sort-val @state)) table-contents)]
    (if (:ascending @state) sorted-contents (rseq sorted-contents))))

(defn sort-indicator [field-name]
  (let [direction (if (:ascending @state) "down" "up")
        img-url   (str "https://cdnjs.cloudflare.com/ajax/libs/foundicons/3.0.0/svgs/fi-arrow-" direction ".svg")]
    (if (= (:sort-val @state) field-name)
      [:img {:src img-url :height "15px" :width "15px"}])))

(defn table []
  [:table
    [:thead
      [:tr
        [:th {:width "200" :on-click #(update-sort-value "first-name")} "First Name" (sort-indicator "first-name")]
        [:th {:width "200" :on-click #(update-sort-value "last-name") } "Last Name"  (sort-indicator "last-name")]
        [:th {:width "200" :on-click #(update-sort-value "known-for") } "Known For"  (sort-indicator "known-for")]]]
    [:tbody
      (for [person (sorted-contents)]
        ^{:key (:id person)} [:tr [:td (:first-name person)] [:td (:last-name person)] [:td (:known-for person)]])]])

(defn table-component []
  [:div {:id "table-wrap"}
    [table]])

(reagent/render-component [table-component]
                          (.getElementById js/document "app"))
