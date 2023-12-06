/**
 * 字典常量
 * @author: clboy
 * @date: 2023-12-06 11:12:26
 * @Copyright (c) 2023 by syl@clboy.cn, All Rights Reserved. 
 */
export class DictItem {
  constructor(label, value, customProp) {
    this.label = label;
    this.value = value;
    Object.assign(this, customProp);
  }
  ve(value) {
    return this.value === value;
  }
  le(label) {
    return this.label === label;
  }
}
export class Dict {
  constructor(origin) {
    this.itemList = Object.values(origin);
    this.origin = origin;
  }
  findByValue(value) {
    return this.itemList.find(item => item.ve(value));
  }
  findByLabel(value) {
    return this.itemList.find(item => item.le(value));
  }
  values() {
    return this.itemList.map(item => item.value);
  }
}

/**
 * 集成类型
 */
export const INTEGRATION_TYPES = {
  FOLDER: new DictItem('文件夹', 'folder', {
    needInstall: false
  }),
  ONLINE_URL: new DictItem('在线网址', 'online_url', {
    needInstall: false
  }),
  DOWNLOAD_URL: new DictItem('文件下载地址', 'download_url', {
    needInstall: true
  }),
  DISK_PATH: new DictItem('本地磁盘路径', 'disk_path', {
    needInstall: true
  })
};
export const INTEGRATION_TYPE_DICT = new Dict(INTEGRATION_TYPES);

/**
 * LibreOffice转换类型
 */
export const LIBREOFFICE_CONVERT_TYPES = {
  DOC: new DictItem('doc', 'doc', {
    canTo: ['pdf', 'html', 'xhtml']
  }),
  PDF: new DictItem('pdf', 'pdf', {
    canTo: ['pdf', 'png', 'jpg']
  })
};
export const LIBREOFFICE_CONVERT_TYPE_DICT = new Dict(LIBREOFFICE_CONVERT_TYPES);