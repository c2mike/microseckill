import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
      md5:'',
      phone:'',
  },
  mutations: {
      setMd5(state,data)
      {
        state.md5 = data;
      },
      setPhone(state,data)
      {
        state.phone = data;
      }

  },
  actions: {
  },
  modules: {
  }
})
