import apiRequest from '../api/apiService';

class GenericDAO {
  constructor(resourcePath) {
    if (!resourcePath) {
      throw new Error('Caminho do recurso não definido');
    }
    this.resourcePath = resourcePath;
  }

  async getAll() {
    return await apiRequest('get', `/${this.resourcePath}`);
  }

  async getById(id) {
    return await apiRequest('get', `/${this.resourcePath}/${id}`);
  }

  async insert(object) {
    return await apiRequest('post', `/${this.resourcePath}/register`, object);
  }

  async update(id, object) {
    return await apiRequest('put', `/${this.resourcePath}/update/${id}`, object);
  }

  async delete(id) {
    return await apiRequest('delete', `/${this.resourcePath}/delete/${id}`);
  }
}

export default GenericDAO;
