(ns task-manager.tasks
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
  (views/index (get-tasks (load-db))))

(defn all []
  (get-tasks (load-db)))

(defn task [number]
  {:body (task->client (get-task (load-db) number))})

(defn update-status [num status]
  (->
   (save (update-task num :task/status (symbol "task.status" status)))
   (get-tasks)))

;; let's be picky about what we update (we could as well use client-> mapping)
(defn update [{number :number status :status description :description}]
  (let [task (update-task number
                     :task/status (symbol "task.status" status)
                     :task/description description)]
   {:status 200}))

(defn create [desc]
  (let [task (create-task desc)]
    {:status 201 :body (task->client task)}))


(defn add-comment [number text]
  (let [tid (:db/id (get-task (load-db) number))]
    (create-comment tid text)
    {:status 200}))
