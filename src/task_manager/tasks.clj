(ns task-manager.tasks
  (:use [compojure.core])
  (:require [task-manager.views :as views :reload true]
            [task-manager.data :as data :reload true]))

(defn index []
  (let [tasks (data/tasks)]
    (views/index tasks)))

(defn with-json-keys [{n :task/number desc :task/description}]
  (hash-map :number n :description desc))

(defn save-task! [num desc]
  (do
    (data/task! num desc)
    (views/index (data/tasks))))

(defn create-task! [desc]
  (do
    (data/create-task! desc)
    (map with-json-keys (data/tasks))))
