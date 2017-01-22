import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/take';

import _ from 'lodash';

export interface Category {
  category_id: number,
  name: string,
  meta_title: string,
  meta_keywords: string,
  page_title: string,
  short_name: string,
  num_children: number,
  level: number,
  parent_id: number,
  grandparent_id: number
}

@Injectable()
export class CategoriesService {

  private categories: Array<Category>;
  private initialized: boolean;

  constructor(public http: Http) {
    this.categories = [];
    this.initialized = false;
  }

  private init(callback) {
    this.http.get('assets/json/clientCategories.json')
      .map(res => res.json())
      .subscribe(data => {
        this.categories = data;
        this.initialized = true;
        callback();
      });
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

  getCategories(parentId: number = null) {
    return this.asserInitialized()
      .then(() => {
        return this.categories.filter(cat => cat.parent_id === parentId);
      });
  }
}
