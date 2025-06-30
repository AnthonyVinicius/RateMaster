import apiRequest from "../api/apiService";
import GenericDAO from "./GenericDAO";

class ProductDAO extends GenericDAO {
  constructor() {
    super("product");
  }

  async getMyProducts() {
    return await apiRequest("get", `/${this.resourcePath}/myProducts`);
  }

  async getProductsReviewedByUser(userId) {
    try {
      const data = await apiRequest("get", `/product/reviewedBy/${userId}`);
      return data;
    } catch (error) {
      return [];
    }
  }
}

export default ProductDAO;
