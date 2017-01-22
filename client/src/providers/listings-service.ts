import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/take';

import { Category } from './categories-service';

import _ from 'lodash';

export interface LocalListing {
  listingId: number,
  title: string,
  categoryPathIds: Array<number>
}

@Injectable()
export class ListingsService {

  private localListings: Array<LocalListing>;
  private initialized: boolean;

  constructor(public http: Http) {
    this.localListings = [];
    this.initialized = false;
  }

  private init(callback) {
    this.http.get('assets/json/clientListingDBSingleLine.json')
      .map(res => res.json())
      .subscribe(data => {
        this.localListings = data;
        this.initialized = true;
        callback();
      });
  }

  private groupListings(listings) {
    return _.chain(listings)
      .groupBy((item) => _.get(item, 'categoryPathIds[0]', -1))
      .mapValues((firstLevelItems) => {
        return _.chain(firstLevelItems)
          .groupBy((item) => _.get(item, 'categoryPathIds[1]', -1))
          .mapValues((secondLevelItems) => {
              return _.chain(secondLevelItems)
                .groupBy((item) => _.get(item, 'categoryPathIds[2]', -1))
                .value();
          })
          .value();
      })
      .value();
  }

  private asserInitialized() {
    return new Promise((resolve) => {
      if (this.initialized) {
        resolve();
      } else {
        this.init(resolve);
      }
    });
  }

  getListingDetails(listingId) {
    return new Promise((resolve) => {
      return {
        listingId: listingId,
        title: 'Mocked title for ' + listingId,
        mainImageUrl: 'https://alicarnold.files.wordpress.com/2009/11/new-product.jpg'
      };
    });
  }

  private findListingsForCategoryPath(categoryPath: Array<Category>) {
    if (categoryPath.length === 0) {
      return [];
    }
    let level1 = _.get(categoryPath, [0, 'category_id'], -1);
    let level2 = _.get(categoryPath, [1, 'category_id'], -1);
    let level3 = _.get(categoryPath, [2, 'category_id'], -1);

    return this.localListings.filter((listing) => {
      let listingCat1 = _.get(listing.categoryPathIds, 0, -1);
      let listingCat2 = _.get(listing.categoryPathIds, 1, -1);
      let listingCat3 = _.get(listing.categoryPathIds, 2, -1);

      let match = listingCat1 === level1;
      if (level2 !== -1) {
        match = match && listingCat2 === level2;
      }
      if (level3 !== -1) {
        match = match && listingCat3 === level3;
      }

      return match;
    });
  }

  getListingsForCategoryPath(categoryPath: Array<Category>, begin: number, count: number) {
    return this.asserInitialized()
      .then(() => {
        let listingsForCategory: Array<LocalListing> = this.findListingsForCategoryPath(categoryPath);
        return listingsForCategory.slice(begin, begin + count);
      });
  }

  getNumberOfListingsForCategoryPath(categoryPath: Array<Category>) {
    return this.asserInitialized()
      .then(() => {
        return this.findListingsForCategoryPath(categoryPath).length;
      });
  }
}
