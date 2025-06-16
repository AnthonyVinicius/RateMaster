import apiRequest from '../api/apiService';
import GenericDAO from './GenericDAO';

class UserDAO extends GenericDAO {
  constructor() {
    super('user');
  }

  async getUserData() {
    return await apiRequest('get', `/${this.resourcePath}/me`);
  }
}

export default UserDAO;