import axios, {AxiosError, AxiosResponse} from "axios";
import {Person} from "@/model/persons";


const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_URL + "/api/v1"
})


export const getAllPersons = async (): Promise<Array<Person>> => {
  return await axiosInstance.get("/personlist")
    .then((response: AxiosResponse<PersonListResponse>) => {
      return response.data.persons
    })
}

export const savePersons = async (persons: Array<Person>, deletedPersonIds: Array<number>): Promise<AxiosResponse> => {
 const payload : SavePersonsPaylod = {
   currentPersons: persons,
   deletedPersonIds: deletedPersonIds
 }
  return await axiosInstance.put("/personlist", payload)
}

interface SavePersonsPaylod{
  currentPersons: Array<Person>,
  deletedPersonIds: Array<number>
}

export interface PersonListResponse {
  persons: Array<Person>
}
