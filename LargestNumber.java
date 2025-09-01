//time complexity: O(n)
//space complexity: O(1)
//this code is used to find the largest number in an array
//easy practice question(1)

import java.util.Scanner;

class LargestNumber {
    public static int largestElement(int[] nums) {
        int max = nums[0];
        for(int i = 1; i < nums.length; i++){ 
            if(nums[i] > max){
                max = nums[i];
            }
        }
        return max;
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int nums[] = new int[n];
        for(int i = 0; i < n; i++){
            nums[i] = sc.nextInt();
        }
        System.out.println(largestElement(nums));
        sc.close(); 
    }
}
