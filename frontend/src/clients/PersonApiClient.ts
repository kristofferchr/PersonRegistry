import axios, {AxiosError, AxiosResponse} from "axios";
import {Person} from "@/model/persons";


const axiosInstance = axios.create({
  baseURL: "http://localhost:8080/api/v1"
})


export const getAllPersons = async (): Promise<Array<Person>> => {
  return await axiosInstance.get("/personlist")
    .then((response: AxiosResponse<PersonListResponse>) => {
      return response.data.persons
    }).catch((error: Error | AxiosError) => {
      console.log(error.message)
      return []
    })
}

export const savePersons = async (persons: Array<Person>, deletedPersonIds: Array<number>): Promise<Array<Person>> => {
 const payload : SavePersonsPaylod = {
   currentPersons: persons,
   deletedPersonIds: deletedPersonIds
 }
  await axiosInstance.put("/personlist", payload)

    .then((response: AxiosResponse<any>) => {
      if (response.status == 204) {
        console.log("Lagret")
      }
    }).catch((error: Error | AxiosError) => {
      console.log(error.message)
    })
}

interface SavePersonsPaylod{
  currentPersons: Array<Person>,
  deletedPersonIds: Array<number>
}

export interface PersonListResponse {
  persons: Array<Person>
}
