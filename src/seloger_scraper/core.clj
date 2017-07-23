(ns seloger-scraper.core
  (:require
   [clojure.string :as string]
   [net.cgrand.enlive-html :as html]
   [org.httpkit.client :as http]
   [sparkledriver.core :refer [with-browser make-browser close-browser!]]
   [seloger-scraper.listing :as listing])
  (:gen-class))

(def browser (make-browser))

(defn fetch-url [url]
  @(http/get url {:as :stream}))

(defn get-link [l]
  (-> l :attrs :href))

(defn get-number-of-pages [url]
  (let [pagination-element (html/select
                            (html/html-resource (:body (fetch-url url)))
                            [:p.pagination_result_number])]
      (if-not (empty? pagination-element)
        (->> pagination-element
             first
             :content
             first
             (re-seq #"[0-9]+")
             second
             Integer.)
        1)))

(defn get-listing-links [url]

  (map get-link (html/select (html/html-resource (:body (fetch-url url))) [:div.title :a])))

(defn scrape-seloger-listings [url]
  (let [listing-links (get-listing-links url)]
    (println "Start scraping " (count listing-links) " listings")
    (map #(listing/scrape browser %) listing-links)))

(defn get-city-id [city]
  (let [city-ids {"montpellier" "340172"}]
      (get city-ids city)))

(defn search [city max-price]
  (let [url (str "http://www.seloger.com/list.htm?tri=initial&idtypebien=2,1&idtt=2&ci=" (get-city-id city) "&pxmax=" max-price "&naturebien=1,2,4")]
    (for [page (range 1 (inc (get-number-of-pages url)))]
      (scrape-seloger-listings (str url "&LISTING-LISTpg=" page)))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
