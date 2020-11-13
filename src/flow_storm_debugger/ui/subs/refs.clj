(ns flow-storm-debugger.ui.subs.refs
  (:require [cljfx.api :as fx]
            [flow-storm-debugger.ui.utils :as utils]
            [editscript.core :as edit.core]
            [editscript.edit :as edit.edit]))

(defn empty-refs? [context]
  (empty? (fx/sub-val context :refs)))

(defn refs [context]
  (fx/sub-val context :refs))

(defn selected-ref [context]
  (let [selected-ref-id (fx/sub-val context :selected-ref-id)
        refs (fx/sub-ctx context refs)]
    (get refs selected-ref-id)))

(defn selected-ref-value-panel-type [context]
  (or (:value-panel-type (fx/sub-ctx context selected-ref))
      :pprint))

(defn selected-ref-value-panel-content [context pprint?]
  (let [{:keys [init-val patches patches-applied]} (fx/sub-ctx context selected-ref)
        val-at-idx (reduce (fn [r p]
                             (edit.core/patch r (edit.edit/edits->script p)))
                           init-val
                           (take patches-applied patches))
        val (if (zero? patches-applied) init-val val-at-idx)]
    
    (if pprint?
      (utils/pprint-form val)

      (-> val
          utils/read-form))))

(defn refs-tabs [context]
  (let [refs (fx/sub-ctx context refs)]
    (->> refs
         (map (fn [[ref-id ref]]
                [ref-id (:ref-name ref)])))))

(defn selected-ref-controls-position [context]
  (let [{:keys [patches-applied patches]} (fx/sub-ctx context selected-ref)]
    {:first? (zero? patches-applied)
     :last? (= patches-applied (count patches))
     :n (inc patches-applied)
     :of (inc (count patches))}))




