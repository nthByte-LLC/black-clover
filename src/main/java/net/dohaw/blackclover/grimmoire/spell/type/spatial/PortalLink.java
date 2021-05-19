package net.dohaw.blackclover.grimmoire.spell.type.spatial;

public class PortalLink {

    private LinkedPortal firstPortal, secondPortal;

    public PortalLink(LinkedPortal firstPortal, LinkedPortal secondPortal){
        this.firstPortal = firstPortal;
        this.secondPortal = secondPortal;
    }

    public PortalLink(){}

    public LinkedPortal getFirstPortal() {
        return firstPortal;
    }

    public LinkedPortal getSecondPortal() {
        return secondPortal;
    }

    public void setPortal(LinkedPortal portal, boolean isFirstPortal){

        LinkedPortal replacedPortal;
        if(isFirstPortal){
            replacedPortal = firstPortal;
            this.firstPortal = portal;
        }else{
            replacedPortal = secondPortal;
            this.secondPortal = portal;
        }

        /*
            Stops the previous portal runner
         */
        if(replacedPortal != null){
            replacedPortal.getPortalDrawer().cancel();
            replacedPortal.getPortalEnterChecker().cancel();
        }

    }

    public LinkedPortal getLink(LinkedPortal portal){
        if(portal.equals(firstPortal)){
            return secondPortal;
        }else if(portal.equals(secondPortal)){
            return firstPortal;
        }else{
            return null;
        }
    }

    public boolean isLinked(){
        return firstPortal != null && secondPortal != null;
    }

}
