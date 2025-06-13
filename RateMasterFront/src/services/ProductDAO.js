import apiRequest from '../api/apiService';
import GenericDAO from './GenericDAO';

class ProductDAO extends GenericDAO {
  constructor() {
    super('product');
  }

  async getMyProducts() {
    return await apiRequest('get', `/${this.resourcePath}/myProducts`);
  }
}

export default ProductDAO;