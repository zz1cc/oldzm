import http from "@/utils/http";

export function getUserInfo() {
  return http.post('center/getCurrentUser', {});
}
