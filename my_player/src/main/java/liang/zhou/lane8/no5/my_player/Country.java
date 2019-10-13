package liang.zhou.lane8.no5.my_player;

public class Country {

    Province provinces[];
    String name;
    public Country(int howManyProvinces){
        provinces=new Province[howManyProvinces];
    }

    public class Province {
        public Province(int howManyCity){
            city=new String[howManyCity];
        }

        int type;
        String typeName;
        String city[];
        String name;
    }
}
