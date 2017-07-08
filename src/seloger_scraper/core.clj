(ns seloger-scraper.core
  (:require
   [clojure.string :as string]
   [net.cgrand.enlive-html :as html]
   [org.httpkit.client :as http]
   [seloger-scraper.listing :as listing])
  (:gen-class))

(defn fetch-url [url]
  @(http/get url {:as :stream}))

(defn get-link [l]
  (-> l :attrs :href))

(defn get-links [url]
  (map get-link (html/select (html/html-resource (:body (fetch-url url))) [:div.title :a])))

(defn scrape-seloger-listings [url]
  (map listing/scrape-listing (get-links url)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
