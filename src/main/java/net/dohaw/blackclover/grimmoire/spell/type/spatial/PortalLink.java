package net.dohaw.blackclover.grimmoire.spell.type.spatial;

public class PortalLink {

    private Portal firstPortal, secondPortal;

    public PortalLink(Portal firstPortal, Portal secondPortal){
        this.firstPortal = firstPortal;
        this.secondPortal = secondPortal;
    }

    public PortalLink(){}

    public Portal getFirstPortal() {
        return firstPortal;
    }

    public Portal getSecondPortal() {
        return secondPortal;
    }

    public void setPortal(Portal portal, boolean isFirstPortal){

        Portal replacedPortal;
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

    public Portal getLink(Portal portal){
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
