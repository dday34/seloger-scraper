(ns seloger-scraper.listing
  (:require
   [sparkledriver.core :refer [with-browser make-browser fetch! find-by-id find-by-xpath* text find-by-class]]))

(defn scrape-listing [url]
  (with-browser [listing-page (fetch! (make-browser) url)]
    {:title (-> listing-page (find-by-id "js-titleAnnonce") text)
     :description (-> listing-page (find-by-id "js-descriptifBien") text)}))


