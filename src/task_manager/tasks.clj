(ns task-manager.tasks
  (:use [compojure.core]
        [task-manager.data :reload true])
  (:require [task-manager.views :as views :reload true]))

(defn smap [source selection]
  (->>
    source
    (map (fn [[from v]]
           (let [[to mapping] (from selection)]
             (cond
               (nil? to) [nil nil] ;; will be filtered out later
               (nil? mapping) [to v] ;; just remap key
               (fn? mapping) [to (mapping v)])) ;; use mapping function
           ))
    (filter #(not= [nil nil] %))
    (into {})))

(defn comment->client [c]
  (smap c {:comment/text [:text]}))

(defn task->client [t]
  (smap t {:db/id [:id]
           :task/number [:number]
           :task/description [:description]
           :task/status [:status #(last (re-find #"/(.+)" (str %)))]
           :comments [:comments #(map comment->client %)]
           :comment/text [:text]}))

(defn client->comment [c]
  (smap c {:text [:comment/text]}))

(defn client->task [t]
  (smap t {:id [:db/id]
           :number [:task/number]
           :description [:task/description]
           :status [:task/status #(symbol "task.status" %)]
           :comments [:comments #(map client->comment %)]}))

(defn get-tasks []
  (->>
    (tasks)
    (sort-by :task/number)
    (map task->client)))

(defn index []
  (views/index (get-tasks)))

(defn all []
  (get-tasks))

(defn task [number]
  {:body (task->client (get-task number))})

(defn update-status [num status]
    (save (update-task num :task/status (symbol "task.status" status)))
    (get-tasks))

;; let's be picky about what we update (we could as well use client-> mapping)
(defn update [{number :number status :status description :description}]
  (save (update-task number
                 :task/status (symbol "task.status" status)
                 :task/description description))
  (get-tasks))

(defn create [desc]
  (do
    (save (create-task desc))
    (get-tasks)))
