(ns task-manager.tasks
  (:use [compojure.core])
  (:require [task-manager.views :as views :reload true]
            [task-manager.data :as data :reload true]))

(defn with-json-keys [{n    :task/number 
                       desc :task/description}]
  (hash-map :number n :description desc))

(defn index []
  (views/index (map with-json-keys (data/tasks))))

(defn save-task! [num desc]
  (do
    (data/task! num desc)
    (views/index (map with-json-keys (data/tasks)))))

(defn create-task! [desc]
  (do
    (data/create-task! desc)
    (map with-json-keys (data/tasks))))
