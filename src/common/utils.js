// 유틸 생성 방법 1.
// export function isEmptyArray(arr) {
//   return !arr || arr.length === 0;
// }

// export default {
//   isEmptyArray,
// };

// import { isEmptyArray } from "@/common/utils";
// if (isEmptyArray(arr)) { ... };

// 유틸 생성 방법 2.
export const utils = {
  isEmptyArray(arr) {
    return !arr || arr.length === 0;
  },
};

// import { utils } from "@/common/utils";
// if (utils.isEmptyArray(arr)) { ... };
