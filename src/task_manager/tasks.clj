(ns task-manager.tasks
  (:refer-clojure :exclude [load])
  (:use [compojure.core]
        [task-manager.data :reload true])
  (:require [task-manager.views :as views :reload true]))

(defn comment->client [c]
  (entity->map c {:comment/text [:text]}))

(defn task->client [t]
  (entity->map t {:db/id [:id]
           :task/number [:number]
           :task/description [:description]
           :task/status [:status #(last (re-find #"/(.+)" (str %)))]
           :comments [:comments #(map comment->client %)]
           :comment/text [:text]}))

(defn client->comment [c]
  (entity->map c {:text [:comment/text]}))

(defn client->task [t]
  (entity->map t {:id [:db/id]
           :number [:task/number]
           :description [:task/description]
           :status [:task/status #(symbol "task.status" %)]
           :comments [:comments #(map client->comment %)]}))

(defn get-tasks [db]
  (->>
   (tasks db)
   (sort-by :task/number)
   (map task->client)))

(defn index []
  (views/index (get-tasks (load))))

(defn all []
  (get-tasks (load)))

(defn task [number]
  {:body (task->client (get-task (load) number))})

(defn update-status [num status]
  (->
   (save (update-task num :task/status (symbol "task.status" status)))
   (get-tasks)))

;; let's be picky about what we update (we could as well use client-> mapping)
(defn update [{number :number status :status description :description}]
  (->
   (save (update-task number
                      :task/status (symbol "task.status" status)
                      :task/description description))
   (get-tasks)))

(defn create [desc]
  (->
    (save (create-task desc))
    (get-tasks)))

(defn add-comment [number text]
  (let [task (get-task (load) number)
        db (save (create-comment (:db/id task) text))]
    (map comment->client (:comments (get-task db number)))))
