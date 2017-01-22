import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';

import { ItemDetailsPage } from '../item-details/item-details';
import { CategoriesService, Category } from '../../providers/categories-service';
import { ListingsService, LocalListing } from '../../providers/listings-service';

import _ from 'lodash';

@Component({
  selector: 'page-list',
  templateUrl: 'list.html'
})
export class ListPage {
  categoryPath: Array<Category>;
  currentCategoryId: number;
  nextCategories: Array<Category>;
  nextCategoriesCollapsed: boolean;
  listings: Array<LocalListing>;
  totalListingsCount: number;

  constructor(public navCtrl: NavController, public navParams: NavParams,
      public categoriesService: CategoriesService, public listingsService: ListingsService) {

    this.categoryPath = navParams.get('categoryPath') || [];
    let lastCategory = this.categoryPath[this.categoryPath.length - 1] || {};
    this.currentCategoryId = _.get(lastCategory, 'category_id', null);
    this.nextCategories = [];
    this.nextCategoriesCollapsed = true;
    this.listings = [];
    this.totalListingsCount = 0;
    this.init();
  }

  private init() {
    if (this.categoryPath.length < 3) {
      this.categoriesService.getCategories(this.currentCategoryId)
      .then((categories) => {
        this.nextCategories = categories;
      });
    }

    this.listingsService.getNumberOfListingsForCategoryPath(this.categoryPath)
    .then((totalListingsCount) => {
      this.totalListingsCount = totalListingsCount;
      this.loadItems(20);
    });
  }

  private loadItems(count: number) {
    return this.listingsService.getListingsForCategoryPath(this.categoryPath, this.listings.length, count)
    .then((listings) => {
      this.listings = _.uniqBy(this.listings.concat(listings), 'listingId');
    });
  }

  toggleCollapse() {
    this.nextCategoriesCollapsed = !this.nextCategoriesCollapsed;
  }

  goToStart() {
    this.navCtrl.setRoot(ListPage, {
      categoryPath: []
    });
  }

  selectCategory(category) {
    let existingIndex = _.findIndex(this.categoryPath, (cat) => cat.category_id === category.category_id);
    let nextCategoryPath = this.categoryPath;
    if (existingIndex === -1) {
      nextCategoryPath = nextCategoryPath.concat([category]);
    } else {
      if (existingIndex === this.categoryPath.length) return;
      nextCategoryPath = nextCategoryPath.slice(0, existingIndex + 1);
    }
    this.navCtrl.setRoot(ListPage, {
      categoryPath: nextCategoryPath
    });
  }

  loadMoreItems(infiniteEvent) {
    console.log('infinite scroll')
    if (this.listings.length < this.totalListingsCount) {
      this.loadItems(10).then(() => infiniteEvent.complete());
    } else {
      infiniteEvent.complete();
      infiniteEvent.enable(false);
    }
  }

  selectItem(item) {
    this.navCtrl.push(ItemDetailsPage, {
      item: item
    });
  }
}
